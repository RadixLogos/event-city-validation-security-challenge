package com.devsuperior.bds04.service;

import com.devsuperior.bds04.dto.RoleDTO;
import com.devsuperior.bds04.dto.UserInsertDTO;
import com.devsuperior.bds04.dto.UserProjection;
import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.RoleRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import com.devsuperior.bds04.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserProjection> userProjections = userRepository.findUserByUsername(username);
        if(userProjections.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        var userData = userProjections.getFirst();
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
        for(UserProjection u : userProjections){
            Role role = new Role(u.getRoleId(),u.getAuthority());
            user.addAuthority(role);
        }
        return user;
    }
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsersPaged(Pageable pageable){
        var users = userRepository.findAll(pageable);
        return users.map(UserDTO::fromUser);
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found"));
        return UserDTO.fromUser(user);
    }

    @Transactional
    public UserDTO insertUser(UserInsertDTO userInsertDTO){
        var user = new User();
        copyDTOToEntity(userInsertDTO.userDTO(), user);
        user.setPassword(encoder.encode(userInsertDTO.password()));
        userRepository.save(user);
        return UserDTO.fromUser(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found!");
        }
        var user =  userRepository.getReferenceById(id);
        copyDTOToEntity(userDTO,user);
        userRepository.save(user);
        return UserDTO.fromUser(user);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found!");
        }
        userRepository.deleteById(id);
    }

    private void copyDTOToEntity(UserDTO userDTO, User user) {
        user.setEmail(userDTO.email());
        user.getAuthorities().clear();
        userDTO.roles().forEach(r ->{
            var role = findRole(r);
            var authority = new Role(role.getId(), role.getAuthority());
            user.addAuthority(authority);
        });
    }

    private Role findRole(RoleDTO r) {
        if(!roleRepository.existsById(r.id())){
            throw new NotFoundException("Authority not found!");
        }
        return roleRepository.getReferenceById(r.id());
    }
}

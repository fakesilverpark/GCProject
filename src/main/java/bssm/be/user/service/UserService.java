package bssm.be.user.service;

import bssm.be.common.exception.BadRequestException;
import bssm.be.common.exception.NotFoundException;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {
}

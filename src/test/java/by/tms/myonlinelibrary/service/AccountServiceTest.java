package by.tms.myonlinelibrary.service;

import by.tms.myonlinelibrary.dto.AccountDto;
import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.entity.Role;
import by.tms.myonlinelibrary.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        AccountDto accountDto = new AccountDto("User", "password123");
        when(passwordEncoder.encode(accountDto.getPassword())).thenReturn("encodedPassword");

        accountService.create(accountDto);

        verify(accountRepository).save(argThat(account ->
                account.getUsername().equals("User") &&
                        account.getPassword().equals("encodedPassword") &&
                        account.getAuthorities().contains(Role.ROLE_USER)));
    }


    @Test
    void becomeAdmin() {
        Account account = new Account();
        account.getAuthorities().add(Role.ROLE_USER);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(
                new UsernamePasswordAuthenticationToken(account, "password123", account.getAuthorities())
        );

        accountService.becomeAdmin(account);

        assertEquals(1, account.getAuthorities().size());
        assertTrue(account.getAuthorities().contains(Role.ROLE_ADMIN), "Must contain ROLE_ADMIN");

        verify(securityContext, times(1)).setAuthentication(argThat(auth ->
                auth.getPrincipal().equals(account) &&
                        auth.getAuthorities().contains(Role.ROLE_ADMIN)
        ));
    }
}
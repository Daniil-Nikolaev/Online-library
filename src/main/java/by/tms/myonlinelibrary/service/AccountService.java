package by.tms.myonlinelibrary.service;

import by.tms.myonlinelibrary.dto.AccountDto;
import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.entity.Role;
import by.tms.myonlinelibrary.repository.AccountRepository;
import by.tms.myonlinelibrary.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(@Valid AccountDto accountDto) {
        if (accountRepository.existsByUsername(accountDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(accountDto.getUsername());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.getAuthorities().add(Role.ROLE_USER);
        accountRepository.save(account);
    }

    public void becomeAdmin(Account account){
        account.getAuthorities().clear();
        account.getAuthorities().add(Role.ROLE_ADMIN);
        accountRepository.save(account);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                account, authentication.getCredentials(), account.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var byUsername = accountRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new UsernameNotFoundException(username);
    }

    public void addBook(Account account,Long bookId){
        var book = bookRepository.findById(bookId).get();
        account.getBooks().add(book);
        accountRepository.save(account);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Account) {
            Account updatedAccount = accountRepository.findById(account.getId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    updatedAccount,
                    authentication.getCredentials(),
                    updatedAccount.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }
}
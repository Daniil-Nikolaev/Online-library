package by.tms.myonlinelibrary.controller;

import by.tms.myonlinelibrary.dto.AccountDto;
import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/registration")
    public String registration() {
        return "reg";
    }

    @PostMapping("/registration")
    public String registration(AccountDto accountDto) {
        accountService.create(accountDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal Account account, Model model) {
        model.addAttribute("account", account);
        return "profile";
    }

    @PostMapping("/profile")
    public String profile(@AuthenticationPrincipal Account account) {
        accountService.becomeAdmin(account);
        return "redirect:/";
    }

    @PostMapping("/add-book")
    public String addBook(@AuthenticationPrincipal Account account, @RequestParam Long bookId) {
        accountService.addBook(account,bookId);
        return "redirect:/account/profile";

    }



}
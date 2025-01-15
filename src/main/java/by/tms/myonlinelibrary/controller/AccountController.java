package by.tms.myonlinelibrary.controller;

import by.tms.myonlinelibrary.dto.AccountDto;
import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String registration(@Valid @ModelAttribute AccountDto accountDto, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "reg";
        }
        try {
            accountService.create(accountDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "reg";
        }

        return "redirect:/account/login";
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
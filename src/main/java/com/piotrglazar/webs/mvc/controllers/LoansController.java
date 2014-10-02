package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.LoanProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.mvc.LoggedInUserProvider;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.LoanOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

@Controller
public class LoansController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String LOANS = "loans";

    private final LoggedInUserProvider userProvider;
    private final LoanProvider loanProvider;
    private final AccountProvider accountProvider;

    @Autowired
    public LoansController(LoggedInUserProvider userProvider, LoanProvider loanProvider, AccountProvider accountProvider) {
        this.userProvider = userProvider;
        this.loanProvider = loanProvider;
        this.accountProvider = accountProvider;
    }

    @RequestMapping("/archiveLoans")
    public String getArchiveLoans(final Model model) {
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, LOANS);
        model.addAttribute("archiveLoans", loanProvider.getAllArchiveLoans(userProvider.getLoggedInUserUsername()));
        return "archiveLoans";
    }

    @RequestMapping("/loans")
    public String getActiveLoans(final Model model) {
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, LOANS);
        model.addAttribute(LOANS, loanProvider.getAllActiveLoans(userProvider.getLoggedInUserUsername()));
        return LOANS;
    }

    @RequestMapping(value = "/newLoan", method = RequestMethod.GET)
    public ModelAndView newLoan(final LoanCreationForm loanCreationForm) {
        final ModelAndView modelAndView = new ModelAndView("newLoan");
        modelAndView.addObject(MvcConfiguration.PAGE_NAME_ATTRIBUTE, LOANS);
        modelAndView.addObject("loanCreationForm", loanCreationForm);
        modelAndView.addObject("allLoanOptions", LoanOption.values());
        modelAndView.addObject("allAccounts", accountProvider.getUserAccounts(userProvider.getLoggedInUserUsername()));
        return modelAndView;
    }

    @RequestMapping(value = "/newLoan/", method = RequestMethod.POST)
    public String newLoan(final Model model, @Valid final LoanCreationForm loanCreationForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allLoanOptions", LoanOption.values());
            model.addAttribute("allAccounts", accountProvider.getUserAccounts(userProvider.getLoggedInUserUsername()));
            return "newLoan";
        }

        LOG.info("Creating loan {}", loanCreationForm);

        loanProvider.createLoan(loanCreationForm, userProvider.getLoggedInUserUsername());

        return "redirect:/loans";
    }

    @RequestMapping(value = "/postponeLoan/{loanId}/", method = RequestMethod.POST)
    public String postponeLoan(@PathVariable("loanId") long loanId) {
        final String username = userProvider.getLoggedInUserUsername();

        if (!loanProvider.postponeLoanIfUserIsItsOwner(username, loanId)) {
            LOG.info("User {} does not own loan {}", username, loanId);
        }
        return "redirect:/loans";
    }
}

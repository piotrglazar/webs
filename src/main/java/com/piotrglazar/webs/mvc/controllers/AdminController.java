package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.business.interest.InterestAccruer;
import com.piotrglazar.webs.business.loans.LoanRepays;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.util.OperationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {

    public static final String UI_MESSAGE = "uiMessage";
    private final MoneyTransferAuditProvider auditProvider;
    private final NewsImporters newsImporters;
    private final InterestAccruer interestAccruer;
    private final LoanRepays loanRepays;

    @Autowired
    public AdminController(MoneyTransferAuditProvider auditProvider, NewsImporters newsImporters, InterestAccruer interestAccruer,
                           LoanRepays loanRepays) {
        this.auditProvider = auditProvider;
        this.newsImporters = newsImporters;
        this.interestAccruer = interestAccruer;
        this.loanRepays = loanRepays;
    }

    @RequestMapping("/admin")
    @OperationLogging(operation = "admin main page")
    public String showMoneyTransferAudit(final Model model) {
        final List<MoneyTransferAuditAdminDto> dtos = auditProvider.findAll();
        model.addAttribute("moneyTransferAudits", dtos);
        model.addAttribute("newsImporters", newsImporters.getNewsImportersNames());
        return "admin";
    }

    @RequestMapping("/admin/importNews/{newsImporterIndex}/")
    @OperationLogging(operation = "admin import news")
    public String importNews(@PathVariable("newsImporterIndex") final int newsImporterIndex, final Model model) {
        newsImporters.fetchNews(newsImporterIndex);
        model.addAttribute(UI_MESSAGE, "News successfully imported");
        return showMoneyTransferAudit(model);
    }

    @RequestMapping("/admin/importAllNews")
    @OperationLogging(operation = "admin import all news")
    public String importAllNews(final Model model) {
        newsImporters.fetchAllNews();
        model.addAttribute(UI_MESSAGE, "All news successfully imported");
        return showMoneyTransferAudit(model);
    }

    @RequestMapping("/admin/interest")
    @OperationLogging(operation = "admin accrue interest")
    public String accrueInterest(final Model model) {
        interestAccruer.accrueInterest();
        model.addAttribute(UI_MESSAGE, "Interest accrued successfully");
        return showMoneyTransferAudit(model);
    }

    @RequestMapping("/admin/loans")
    @OperationLogging(operation = "admin repay loans")
    public String repayLoans(final Model model) {
        loanRepays.repayAllLoans();
        model.addAttribute(UI_MESSAGE, "Loans repaid successfully");
        return showMoneyTransferAudit(model);
    }
}

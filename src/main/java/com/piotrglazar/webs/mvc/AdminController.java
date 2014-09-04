package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.business.InterestAccruer;
import com.piotrglazar.webs.business.LoanRepays;
import com.piotrglazar.webs.business.NewsImporters;
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
        model.addAttribute("uiMessage", "News successfully imported");
        return showMoneyTransferAudit(model);
    }

    @RequestMapping("/admin/interest")
    @OperationLogging(operation = "admin accrue interest")
    public String accrueInterest(final Model model) {
        interestAccruer.accrueInterest();
        model.addAttribute("uiMessage", "Interest accrued successfully");
        return showMoneyTransferAudit(model);
    }

    @RequestMapping("/admin/loans")
    @OperationLogging(operation = "admin repay loans")
    public String repayLoans(final Model model) {
        loanRepays.repayAllLoans();
        model.addAttribute("uiMessage", "Loans repaid successfully");
        return showMoneyTransferAudit(model);
    }
}

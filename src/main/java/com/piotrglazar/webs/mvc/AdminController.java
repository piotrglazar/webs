package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.business.NewsImporters;
import com.piotrglazar.webs.dto.MoneyTransferAuditDto;
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

    @Autowired
    public AdminController(final MoneyTransferAuditProvider auditProvider, final NewsImporters newsImporters) {
        this.auditProvider = auditProvider;
        this.newsImporters = newsImporters;
    }

    @RequestMapping("/admin")
    @OperationLogging(operation = "admin main page")
    public String showMoneyTransferAudit(final Model model) {
        final List<MoneyTransferAuditDto> dtos = auditProvider.findAll();
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
}

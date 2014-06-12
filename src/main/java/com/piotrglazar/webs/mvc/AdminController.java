package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.dto.MoneyTransferAuditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {

    private final MoneyTransferAuditProvider auditProvider;

    @Autowired
    public AdminController(final MoneyTransferAuditProvider auditProvider) {
        this.auditProvider = auditProvider;
    }

    @RequestMapping("/admin")
    public String showMoneyTransferAudit(final Model model) {
        final List<MoneyTransferAuditDto> dtos = auditProvider.findAll();
        model.addAttribute("moneyTransferAudits", dtos);
        return "admin";
    }
}

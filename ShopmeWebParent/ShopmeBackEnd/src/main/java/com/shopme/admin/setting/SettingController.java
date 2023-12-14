package com.shopme.admin.setting;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {
    @Autowired private SettingService service;
    @Autowired private CurrencyRepository currencyRepo;

    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> listSettings = service.listAllSettings();
        List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(),setting.getValue());
        }
        model.addAttribute("listCurrencies",listCurrencies);
        return "setting/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                              HttpServletRequest request, RedirectAttributes ra) throws IOException {
        GeneralSettingBag settingBag = service.getGeneralSettings();

        for (Setting setting : settingBag.list()) {
            System.out.println(setting.getKey());
        }


        saveSiteLogo(multipartFile,settingBag);
        saveCurrencySymbol(request,settingBag);
        updateSettingValuesFromForm(request,settingBag.list());

        ra.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSetting(HttpServletRequest request,RedirectAttributes ra) {
        List<Setting> settings = service.getMailServerSettings();
        updateSettingValuesFromForm(request,settings);
        ra.addFlashAttribute("message", "Mail server settings have been saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSetting(HttpServletRequest request,RedirectAttributes ra) {
        List<Setting> settings = service.getMailTemplatesSettings();
        updateSettingValuesFromForm(request,settings);
        ra.addFlashAttribute("message", "Mail template settings have been saved");
        return "redirect:/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());

            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAll(listSettings);
    }


    public void saveSiteLogo(MultipartFile multipartFile,GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;


            settingBag.updateSiteLogo(value);
            String uploadDir = "site-logo";

            FileUploadUtil.removeDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }
    }
    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }
}

package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired private SettingRepository repo;

    public List<Setting> getGeneralSettings() {
        return repo.findByTwoCategories(SettingCategory.CURRENCY, SettingCategory.GENERAL);
    }
}

package com.shopme.admin.setting;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;
import com.shopme.common.entity.setting.SettingCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

    @Autowired private SettingRepository repo;

    @Test
    public void testCreateSetting() {
        Setting setting = new Setting("ORDER_CONFIRMATION_CONTENT","2",SettingCategory.MAIL_TEMPLATE);
        Setting saved = repo.save(setting);

        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void testGet() {
        SettingBag settingBag = new SettingBag((List<Setting>) repo.findAll());
        int index = ((List<Setting>) repo.findAll()).indexOf(new Setting("THOUSANDS_POINT_TYPE"));
        System.out.println(index);

    }

    @Test
    public void testUpdate() {
        Setting setting = repo.findById("SMTP_SECURED").get();
        setting.setCategory(SettingCategory.MAIL_SERVER);
        repo.save(setting);

    }
}

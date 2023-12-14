package com.shopme.admin.shippingrate;

import com.shopme.admin.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShippingRateService {
    public static final int SHIPPINGRATE_PER_PAGE = 10;
    @Autowired private ShippingRateRepository repo;
    @Autowired private CountryRepository countryRepo;

    public Page<ShippingRate> listByPage(int pageNum, String sortField, String sortDir,String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, SHIPPINGRATE_PER_PAGE, sort);

        if (keyword != null) {
            return repo.findAll(keyword, pageable);
        }
        return repo.findAll(pageable);

    }

    public void updateEnabledStatus(Integer id, boolean enabled) {
        repo.enabled(id,enabled);
    }

    public List<Country> listCountries() {
        return (List<Country>) countryRepo.findAll();
    }

    public void save(ShippingRate shippingRate) throws ShippingRateAlreadyExistsException {
        ShippingRate srInDB = repo.findByCountryAndState(shippingRate.getCountry().getId(),shippingRate.getState());
        boolean foundExistingRateInNewMode = shippingRate.getId() == null && srInDB != null;
        boolean foundDifferentExistingRateInEditMode = shippingRate.getId() != null && shippingRate != null;

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException("there's already a rate for the destination");
        }
        repo.save(shippingRate);
    }

    public ShippingRate get(Integer id) {
       return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}

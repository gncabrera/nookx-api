package com.dot.collector.api.service;

import com.dot.collector.api.domain.Currency;
import com.dot.collector.api.repository.CurrencyRepository;
import com.dot.collector.api.service.dto.CurrencyDTO;
import com.dot.collector.api.service.mapper.CurrencyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dot.collector.api.domain.Currency}.
 */
@Service
@Transactional
public class CurrencyService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    public CurrencyDTO save(CurrencyDTO currencyDTO) {
        LOG.debug("Request to save Currency : {}", currencyDTO);
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDto(currency);
    }

    public CurrencyDTO update(CurrencyDTO currencyDTO) {
        LOG.debug("Request to update Currency : {}", currencyDTO);
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDto(currency);
    }

    public Optional<CurrencyDTO> partialUpdate(CurrencyDTO currencyDTO) {
        LOG.debug("Request to partially update Currency : {}", currencyDTO);

        return currencyRepository
            .findById(currencyDTO.getId())
            .map(existingCurrency -> {
                currencyMapper.partialUpdate(existingCurrency, currencyDTO);

                return existingCurrency;
            })
            .map(currencyRepository::save)
            .map(currencyMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CurrencyDTO> findAll() {
        LOG.debug("Request to get all Currencies");
        return currencyRepository.findAll().stream().map(currencyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<CurrencyDTO> findOne(Long id) {
        LOG.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id).map(currencyMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Currency : {}", id);
        currencyRepository.deleteById(id);
    }
}

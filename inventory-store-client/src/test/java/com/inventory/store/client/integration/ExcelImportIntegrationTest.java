package com.inventory.store.client.integration;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.repository.IItemRepository;
import com.inventory.store.client.service.ExcelItemImportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para la importación de Excel
 *
 * @author eucsina on 28/04/2026
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class ExcelImportIntegrationTest {
}

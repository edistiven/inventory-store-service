package com.inventory.store.client.service;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.repository.IItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

/**
 * Test class for importing items from Excel file to INV_ITEM table
 *
 * @author eucsina on 28/04/2026
 */
@ExtendWith(MockitoExtension.class)
@Slf4j
class ExcelItemImportTest {

    @Mock
    private IItemRepository itemRepository;

    @InjectMocks
    private ExcelItemImportService excelItemImportService;

    private List<ItemEntity> mockItems;
}

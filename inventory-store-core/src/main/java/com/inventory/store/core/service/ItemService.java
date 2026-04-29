package com.inventory.store.core.service;

import com.inventory.store.client.entity.ItemEntity;
import com.inventory.store.client.repository.IItemRepository;
import com.inventory.store.client.services.IItemService;
import com.inventory.store.core.component.UserContextService;
import com.inventory.store.request.ItemRequestVO;
import com.inventory.store.vo.EstadisticasInventarioVO;
import com.inventory.store.vo.ItemVO;
import com.inventory.store.vo.PaginatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * Item service implementation.
 */
@Lazy
@Service
@Slf4j
public class ItemService extends GenericService<ItemEntity, IItemRepository> implements IItemService {

    @Autowired
    @Lazy
    private UserContextService userContextService;

    /**
     * Constructor.
     */
    public ItemService(IItemRepository repository) {
        super(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ItemVO> getItemsPaginated(ItemRequestVO itemRequest) {
        return repository.getItemsPaginated(itemRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveItem(ItemVO itemVO) {
        // Crear la entidad ItemEntity para guardar en la base de datos
        ItemEntity newItemEntity = ItemEntity.builder()
                .itemCode(itemVO.getItemCode())
                .barCode(itemVO.getBarCode())
                .itemName(itemVO.getName())
                .itemDescription(itemVO.getDescription())
                .price(itemVO.getPrice())
                .quantity(itemVO.getQuantity())
                .soldQuantity(0)
                .catalogueId(itemVO.getCatalogueId())
                .expirationDate(itemVO.getExpirationDate())
                .registerUserDate(userContextService.getCurrentUser()) // Usar el usuario actual
                .registerDate(new Date()) // Usar el usuario actual
                .build();

        // Guardar el producto en la base de datos
        repository.save(newItemEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateItem(ItemVO itemVO) {
        ItemEntity itemEntity = repository.findById(itemVO.getItemCode());

        if (itemEntity == null) {
            throw new RuntimeException("Item no encontrado con itemCode " + itemVO.getItemCode());
        }
        // Actualizar los campos
        itemEntity.setItemName(itemVO.getName());
        itemEntity.setItemDescription(itemVO.getDescription());
        itemEntity.setPrice(itemVO.getPrice());
        itemEntity.setQuantity(itemVO.getQuantity());
        itemEntity.setExpirationDate(itemVO.getExpirationDate());
        itemEntity.setBarCode(itemVO.getBarCode());
        itemEntity.setCatalogueId(itemVO.getCatalogueId());

        // Obtener el usuario que realiza la actualización
        String currentUser = userContextService.getCurrentUser();
        itemEntity.setModificationUserDate(currentUser);
        itemEntity.setModificationDate(new Date());
        itemEntity.setStatus(itemVO.getStatus());

        // Guardar en la base de datos
        repository.update(itemEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemVO getItemByItemCode(Long itemCode) {
        return repository.getItemByItemCode(itemCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteItem(Long itemCode) {
        log.info("Cambiando estado del producto con itemCode: {}", itemCode);

        // Buscar el item en la base de datos
        ItemEntity itemEntity = repository.findById(itemCode);

        if (itemEntity == null) {
            throw new RuntimeException("Item no encontrado con itemCode " + itemCode);
        }

        // Cambiar el estado
        boolean nuevoEstado = !itemEntity.getStatus();
        itemEntity.setStatus(nuevoEstado);

        // Obtener el usuario que realiza la actualización
        String currentUser = userContextService.getCurrentUser();
        itemEntity.setModificationUserDate(currentUser);
        itemEntity.setModificationDate(new Date());

        // Guardar en la base de datos
        repository.update(itemEntity);

        log.info("Estado del producto {} cambiado a: {}", itemCode, nuevoEstado ? "ACTIVO" : "INACTIVO");
        return nuevoEstado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemVO> getActiveItems(String name) {
        return repository.getActiveItems(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EstadisticasInventarioVO getInventoryStatistics() {
        log.info("Obteniendo estadísticas del inventario");
        
        // Obtener todos los items activos
        List<ItemVO> itemsActivos = repository.getActiveItems("");
        
        // Calcular estadísticas básicas
        long totalItems = itemsActivos.size();
        int stockTotal = itemsActivos.stream()
                .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                .sum();
        
        BigDecimal valorTotal = itemsActivos.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                    int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcular stock saludable y bajo (70% y 30% del total)
        int stockSaludable = (int) Math.round(stockTotal * 0.7);
        int stockBajo = (int) Math.round(stockTotal * 0.3);
        
        // Calcular promedios
        double promedioStockPorProducto = totalItems > 0 ? (double) stockTotal / totalItems : 0.0;
        BigDecimal valorPromedioPorProducto = totalItems > 0 ? 
                valorTotal.divide(BigDecimal.valueOf(totalItems), 2, RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
        
        return EstadisticasInventarioVO.builder()
                .totalItems(totalItems)
                .stockTotal(stockTotal)
                .valorTotal(valorTotal)
                .stockSaludable(stockSaludable)
                .stockBajo(stockBajo)
                .promedioStockPorProducto(promedioStockPorProducto)
                .valorPromedioPorProducto(valorPromedioPorProducto)
                .build();
    }


}

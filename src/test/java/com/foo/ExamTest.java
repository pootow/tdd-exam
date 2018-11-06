package com.foo;

import com.foo.impl.ItemServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExamTest {

    private static final Logger logger = LoggerFactory.getLogger(ExamTest.class);

    private static List<String> skuIds;

    /**
     * 构造100个 skuid 作为测试条件
     */
    @BeforeClass
    public static void setUp() {
        skuIds = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            skuIds.add(String.valueOf(i));
        }
        ServiceBeanFactory factory = ServiceBeanFactory.getInstance();
        factory.registerServiceBean(ItemService.class,
                new ItemServiceImpl(
                        factory.getServiceBean(SkuService.class),
                        factory.getServiceBean(PriceService.class),
                        factory.getServiceBean(InventoryService.class)
                ));
    }




    private ItemService itemService;
//    private SkuService skuService;

    public ExamTest() {
        itemService = ServiceBeanFactory.getInstance().getServiceBean(ItemService.class);
//        skuService = ServiceBeanFactory.getInstance().getServiceBean(SkuService.class);
    }


    @AfterClass
    public static void tearDown() {
        skuIds = null;
    }


    @Test
    public void testItemService() {
        List<ItemDTO> items = itemService.getBySkuIds(skuIds);
        assertEquals(1, 1);
    }

    @Test
    public void singleOriginalSKUCase() {

        logger.trace("mock sku service");
        SkuService skuService = mock(SkuService.class);
        SkuInfoDTO theSKU = genSKU(theSKU1 -> {
            theSKU1.setSkuType("");
            theSKU1.setArtNo("");
        });
        final List<String> ids = Collections.singletonList("1");
        when(skuService.findByIds(new ArrayList<>(ids)))
                .thenReturn(new ArrayList<>(Collections.singletonList(theSKU)));

        logger.trace("mock price service");
        PriceService priceService = mock(PriceService.class);
        when(priceService.getBySkuId(anyString())).thenReturn(new BigDecimal("10.01"));

        logger.trace("mock inventory service");
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.getBySkuId(anyString())).thenReturn(new ArrayList<>(Arrays.asList(
                new ChannelInventoryDTO("ch1", new BigDecimal("1.01")),
                new ChannelInventoryDTO("ch2", new BigDecimal("2.02"))
        )));

        logger.trace("create SUT");
        itemService = new ItemServiceImpl(skuService, priceService, inventoryService);


        logger.trace("do a biz");
        List<ItemDTO> items = itemService.getBySkuIds(ids);

        logger.trace("assertions");
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("art1", items.get(0).getName());
        assertEquals(new BigDecimal("1.0"), items.get(0).getInventory());
    }

    private SkuInfoDTO genSKU(Consumer<SkuInfoDTO> init) {
        SkuInfoDTO theSKU = new SkuInfoDTO();
        theSKU.setId("1");
        theSKU.setName("name of 1");
        init.accept(theSKU);
        return theSKU;
    }
}

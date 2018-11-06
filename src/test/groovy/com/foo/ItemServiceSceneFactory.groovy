package com.foo

import com.foo.impl.ItemServiceImpl
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class ItemServiceSceneFactory extends Specification {
    Map<String, SKUFixture> skuMap

    ItemServiceScene createFromSKUFixtures(List<SKUFixture> skus) {
        skuMap = skus.collectEntries { [it.id, it] }

        SkuService skuService = Stub {
            findByIds(_) >> { args ->

                def ids = args[0]

                return skuMap.findAll { it.key in ids }.collect {
                    new SkuInfoDTO(
                            id: it.value.id,
                            name: it.value.name,
                            artNo: it.value.artNo,
                            spuId: it.value.spuId,
                            skuType: it.value.type,
                    )
                }
            }
        }

        PriceService priceService = Stub {
            getBySkuId(_) >> { String id ->
                skuMap[id].price
            }
        }

        InventoryService inventoryService = Stub {
            getBySkuId(_) >> { String id ->
                skuMap[id].inventory.collect {
                    new ChannelInventoryDTO(it.channel, it.amount)
                }
            }
        }

        new ItemServiceScene(
                itemService: new ItemServiceImpl(
                        skuService,
                        priceService,
                        inventoryService
                ),
                priceService: priceService,
                inventoryService: inventoryService,
        )
    }
}

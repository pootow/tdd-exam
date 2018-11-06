package com.foo

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class ExamSpec extends Specification {
    def "single sku should be the same as item"() {
        given:

        ItemServiceScene scene = new ItemServiceSceneFactory().createFromSKUFixtures([new SKUFixture(
                id: id,
                type: type,
                name: name,
                artNo: artNo,
                spuId: spuId,
                price: new BigDecimal(price),
                inventory: inventories.collect {
                    new InventoryFixture(channel: it.key, amount: new BigDecimal(it.value))
                }
        )])

        and:

        def items = scene.itemService.getBySkuIds([id])

        def item = items[0]

        expect:

        items.size() == 1
        item.name == name
        item.artNo == artNo
        item.inventory == new BigDecimal(inventoriesSum)
        item.maxPrice == new BigDecimal(maxPrice)
        item.minPrice == new BigDecimal(minPrice)
        item.spuId == spuId

        where:

        id  | name    | type             | artNo   | spuId     | price   | inventoriesSum | maxPrice | minPrice | inventories
        '1' | 'sku-1' | SKUType.ORIGINAL | 'art-1' | 'spuId-1' | '10.01' | '3.03'         | '10.01'  | '10.01'  | [ch1: '1.01', ch2: '2.02']
        '2' | 'sku-2' | SKUType.DIGITAL  | 'art-2' | 'spuId-2' | '20.02' | '2.02'         | '20.02'  | '20.02'  | [ch1: '2.02']

    }

    def "multi sku should agg inventory and price for item"() {
        given:

        ItemServiceScene scene = new ItemServiceSceneFactory().createFromSKUFixtures([new SKUFixture(
                id: id,
                type: type,
                name: name,
                artNo: artNo,
                spuId: spuId,
                price: new BigDecimal(price),
                inventory: inventories.collect {
                    new InventoryFixture(channel: it.key, amount: new BigDecimal(it.value))
                }
        )])

        def items = scene.itemService.getBySkuIds(['1'])

        def item = items[0]

        expect:

        items.size() == 1
        item.name == name
        item.artNo == artNo
        item.inventory == new BigDecimal(inventoriesSum)
        item.maxPrice == new BigDecimal(maxPrice)
        item.minPrice == new BigDecimal(minPrice)
        item.spuId == spuId

        log.debug("other:${other}")

        where:

        [
                id, name, type, artNo, spuId, price, inventoriesSum, maxPrice, minPrice, inventories] << [
                ['1', 'sku-1', SKUType.ORIGINAL, 'art-1', 'spuId-1', '10.01', '3.03', '10.01', '10.01', [ch1: '1.01', ch2: '2.02']],
        ]

        where:
        [other] << [1,2,3]


    }
}

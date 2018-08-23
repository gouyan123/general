package com.dongnao.mark.lock.readwritelock;
/*定义商品类*/
public class GoodsVo {
    private final String id;
    private int totalSaleNumber;//总销售数
    private int depotNumber;//当前库存数
    public GoodsVo(String id, int totalSaleNumber, int depotNumber) {
        this.id = id;
        this.totalSaleNumber = totalSaleNumber;
        this.depotNumber = depotNumber;
    }
    public int getTotalSaleNumber() {
        return totalSaleNumber;
    }
    public int getDepotNumber() {
        return depotNumber;
    }
    public void setGoodsVoNumber(int changeNumber){
        this.totalSaleNumber += changeNumber;
        this.depotNumber -= changeNumber;
    }
}

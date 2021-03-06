package com.coding.sales.product;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Product {
    public ArrayList<String> mDiscountCard = new ArrayList<String>();
    public ArrayList<String> mActivity = new ArrayList<String>();
    public BigDecimal mPrice;
    public int mCount;
    public String productNo;
    public String productName;
    public int amount;

    public ArrayList<String> mDiscountCardUsed = new ArrayList<String>();
    public String mActivityType;

    public BigDecimal mTotalPrice;// 优惠总价格
    public BigDecimal mTotalOriginPrice;// 总价格

    public BigDecimal doBuy(ArrayList<String> discounts) {
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(discounts);
        BigDecimal discount = getTotalPriceByDiscount(temp, false);
        BigDecimal manjian = getTotalPriceByActivity(false);
        BigDecimal tt = null;
        if (discount.doubleValue() - manjian.doubleValue() > 0) {
            tt = getTotalPriceByActivity(true);
        } else {
            tt = getTotalPriceByDiscount(discounts, true);
        }
        return tt;
    }

    public BigDecimal getTotalPriceByDiscount(ArrayList<String> discounts, boolean real) {
        mTotalOriginPrice = mPrice.multiply(new BigDecimal(mCount));
        BigDecimal price = mTotalOriginPrice;
        for (String t : mDiscountCard) {
            BigDecimal p = mPrice;
            if (discounts.contains(t)) {
                discounts.remove(t);
                price = getPriceByDiscount(mTotalOriginPrice, t);
                if (real) {
                    mDiscountCardUsed.add(t);
                }
            }
        }

        if (real) {
            mTotalPrice = price;
        }
        return price;
    }

    public BigDecimal getTotalPriceByActivity(boolean real) {
        BigDecimal price = mPrice.multiply(new BigDecimal(mCount));
        BigDecimal priceJian = null;
        String typeJian = null;
        if (mActivity.contains("每满1000元减10")) {
            if (price.compareTo(new BigDecimal("1000")) > -1) {
                priceJian = price.subtract(new BigDecimal("10"));
                typeJian = "每满1000元减10";
            }
        }
        if (mActivity.contains("每满2000元减30")) {
            if (price.compareTo(new BigDecimal("2000")) > -1) {
                priceJian = price.subtract(new BigDecimal("30"));
                typeJian = "每满2000元减30";
            }
        }
        if (mActivity.contains("每满3000元减350")) {
            if (price.compareTo(new BigDecimal("3000")) > -1) {
                priceJian = price.subtract(new BigDecimal("350"));
                typeJian = "每满3000元减350";
            }
        }
        if (priceJian == null) {
            priceJian = price;
        }
        BigDecimal priceShan = null;
        String typeShan = null;
        if (mCount >= 4) {
            if (mActivity.contains("满3送1")) {
                priceShan = mPrice.multiply(new BigDecimal(mCount - 1));
                typeShan = "满3送1";
            } else if (mActivity.contains("第3件半价")) {
                priceShan = mPrice.multiply(new BigDecimal(mCount)).subtract(mPrice.multiply(new BigDecimal("0.5")));
                typeShan = "满3送1";
            }

        } else if (mCount == 3 && mActivity.contains("第3件半价")) {
            priceShan = mPrice.multiply(new BigDecimal(mCount)).subtract(mPrice.multiply(new BigDecimal("0.5")));
            typeShan = "第3件半价";
        }
        if (priceShan == null) {
            priceShan = price;
        }

        if (priceShan.compareTo(priceJian) > -1) {
            price = priceJian;
            if (real) {
                mActivityType = typeJian;
            }
        } else {
            price = priceShan;
            if (real) {
                mActivityType = typeShan;
            }
        }
        if (real) {
            mTotalPrice = price;
        }
        return price;
    }

    private BigDecimal getPriceByDiscount(BigDecimal p, String type) {
        if ("95折券".equals(type)) {
            return p.multiply(new BigDecimal("0.95"));
        } else if ("9折券".equals(type)) {
            return p.multiply(new BigDecimal("0.9"));
        } else {
            return p;
        }
    }
}
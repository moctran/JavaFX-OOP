package com.app.dict.crawl.linkers;

import com.app.dict.base.Model;
import com.app.dict.base.NhanVatModel;
import com.app.dict.base.ThoiKyModel;
import com.app.dict.crawl.crawlers.NhanVatCrawler;
import com.app.dict.crawl.crawlers.ThoiKyCrawler;
import com.google.gson.reflect.TypeToken;
import com.app.dict.util.Config;
import java.util.*;


public class NhanVatToThoiKy
{
    private Map<String, List<String>> generateHashMap()
    {
        Map<String, List<String>> hashMap = new HashMap<>();
        ThoiKyCrawler thoiKyCrawler = new ThoiKyCrawler();
        List<ThoiKyModel> thoiKyList = thoiKyCrawler.loader(Config.THOI_KY_FILENAME,  new TypeToken<List<ThoiKyModel>>() {});

        for (ThoiKyModel thoiKy : thoiKyList)
        {
            for (String nhanVat : thoiKy.getCacNhanVatLienQuan())
            {
                if (!hashMap.containsKey(nhanVat))
                {
                    hashMap.put(nhanVat, new ArrayList<>());
                }
                hashMap.get(nhanVat).add(thoiKy.getCode());
            }
        }

        return hashMap;
    }

    public void linkNhanVatToThoiKy()
    {
        Map<String, List<String>> nhanVatToThoiKy = generateHashMap();
        System.out.println(nhanVatToThoiKy);
        NhanVatCrawler nhanVatCrawler = new NhanVatCrawler();
        List<NhanVatModel> nhanVatList = nhanVatCrawler
                .loader(Config.NHAN_VAT_LICH_SU_FILENAME,  new TypeToken<List<NhanVatModel>>() {});

        for (NhanVatModel nhanVat : nhanVatList)
        {
            if (nhanVatToThoiKy.containsKey(nhanVat.getCode()))
            {
                Set<String> set = new HashSet<>(nhanVatToThoiKy.get(nhanVat.getCode()));
                nhanVat.setCacThoiKyLienQuan(set);
            }
        }

        List<Model> models = new ArrayList<>();
        models.addAll(nhanVatList);
        nhanVatCrawler.writeJson(Config.NHAN_VAT_LICH_SU_FILENAME, models);
    }

    public static void main(String[] args)
    {
        NhanVatToThoiKy nhanVatToThoiKy = new NhanVatToThoiKy();
        nhanVatToThoiKy.linkNhanVatToThoiKy();
    }
}

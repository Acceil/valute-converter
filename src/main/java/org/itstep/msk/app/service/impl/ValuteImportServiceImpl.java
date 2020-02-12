package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Rate;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.repository.RateRepository;
import org.itstep.msk.app.repository.ValuteRepository;
import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ValuteImportServiceImpl implements ValuteImportService {
    final private static String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    private ValuteRepository valuteRepository;
    private RateRepository rateRepository;

    private List<Valute> valutesFromDb;
    private Map<String, List<Rate>> ratesFromDb = new HashMap<>();

    @Autowired
    public ValuteImportServiceImpl(
            ValuteRepository valuteRepository,
            RateRepository rateRepository
    ) {
        this.valuteRepository = valuteRepository;
        this.rateRepository = rateRepository;
    }

    @Override
    public void importValutes() {
        GregorianCalendar date = new GregorianCalendar();
        importValutes(date);
    }

    @Override
    public void importValutes(GregorianCalendar date) {
        String url = getUrl(date);
        NodeList valutesNodes = getValutesNodes(url);

        for (int i = 0; i < valutesNodes.getLength(); i++) {
            Element valuteNode = (Element) valutesNodes.item(i);
            String valuteCbrId = valuteNode.getAttribute("ID");

            Valute valute = getValuteByCbrId(valuteCbrId);
            if (valute == null) {
                valute = saveValute(valuteNode);
            }

            Rate rate = getRateByCbrIdAndDate(valuteCbrId, date);
            if (rate == null) {
                saveRate(
                        valute,
                        date,
                        valuteNode
                );
            }
        }

        valuteRepository.flush();
        rateRepository.flush();
    }

    private String getUrl(GregorianCalendar date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setCalendar(date);
        String dateString = format.format(date.getTime());

        return CBR_URL + dateString;
    }

    private NodeList getValutesNodes(String url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(url);

            return document
                    .getDocumentElement()
                    .getElementsByTagName("Valute");
        } catch (Exception e) {
            e.printStackTrace();

            return createEmptyNodeList();
        }
    }

    private NodeList createEmptyNodeList() {
        return new NodeList() {
            @Override
            public Node item(int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };
    }

    private List<Valute> getValutesFromDb() {
        if (valutesFromDb == null) {
            valutesFromDb = valuteRepository.findAll(
                    Sort.by("name")
            );
        }

        return valutesFromDb;
    }

    private Valute getValuteByCbrId(String cbrId) {
        return getValutesFromDb()
                .stream()
                .filter(v -> v.getCbrId().equals(cbrId))
                .findFirst()
                .orElse(null);
//        Optional<Valute> result;
//        result.isPresent() ? result.get() : null;
//        result.orElse(null);
    }

    private List<Rate> getRatesFromDb(GregorianCalendar date) {
        String key = getRateKey(date);

        if (!ratesFromDb.keySet().contains(key)) {
            List<Rate> rates = rateRepository.findAllByDateOrderByValueAsc(date.getTime());
            ratesFromDb.put(key, rates);
        }

        return ratesFromDb.get(key);
    }

    private String getRateKey(GregorianCalendar date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setCalendar(date);

        return format.format(date.getTime());
    }

    private Rate getRateByCbrIdAndDate(String cbrId, GregorianCalendar date) {
        return getRatesFromDb(date)
                .stream()
                .filter(r -> r.getValute().getCbrId().equals(cbrId))
                .findFirst()
                .orElse(null);
    }

    private Valute saveValute(Element valuteNode) {
        Valute valute = new Valute();
        valute.setCbrId(
                valuteNode.getAttribute("ID")
        );
        valute.setName(
                valuteNode
                        .getElementsByTagName("Name")
                        .item(0)
                        .getTextContent()
        );
        valute.setNumCode(
                Integer.valueOf(
                        valuteNode
                                .getElementsByTagName("NumCode")
                                .item(0)
                                .getTextContent()
                )
        );
        valute.setCharCode(
                valuteNode
                        .getElementsByTagName("CharCode")
                        .item(0)
                        .getTextContent()
        );
        valute.setNominal(
                Integer.valueOf(
                        valuteNode
                                .getElementsByTagName("Nominal")
                                .item(0)
                                .getTextContent()
                )
        );

        valuteRepository.save(valute);

        return valute;
    }

    private void saveRate(Valute valute, GregorianCalendar date, Element valuteNode) {
        Rate rate = new Rate();
        rate.setValute(valute);
        rate.setDate(date.getTime());
        rate.setValue(
                Double.valueOf(
                        valuteNode
                                .getElementsByTagName("Value")
                                .item(0)
                                .getTextContent()
                                .replace(',', '.')
                )
        );

        rateRepository.save(rate);
    }
}

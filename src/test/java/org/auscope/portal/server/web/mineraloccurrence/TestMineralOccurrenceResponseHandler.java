package org.auscope.portal.server.web.mineraloccurrence;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Collection;

import junit.framework.Assert;

/**
 * User: Mathew Wyatt
 * Date: 24/03/2009
 * Time: 10:24:29 AM
 */
public class TestMineralOccurrenceResponseHandler {

    @Test
    public void testHandleMineResponse() throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        File mineGetFeatureResponse = new File("src/test/resources/mineGetFeatureResponse.xml");
        BufferedReader reader = new BufferedReader( new FileReader(mineGetFeatureResponse) );
        StringBuffer mineGetFeatureResponseXML = new StringBuffer();

        String str;
        while ((str = reader.readLine()) != null) {
            mineGetFeatureResponseXML.append(str);
        }
        reader.close();

        Collection<Mine> mines = MineralOccurrencesResponseHandler.getMines(mineGetFeatureResponseXML.toString());

        Assert.assertEquals("There are 2 mines", 2, mines.size());
        Assert.assertEquals("The first one is Good Hope", "Good Hope", ((Mine)mines.toArray()[0]).getMineNamePreffered());
        Assert.assertEquals("The second one is Sons of Freedom Reef", "Sons of Freedom Reef", ((Mine)mines.toArray()[1]).getMineNamePreffered());
    }

    @Test
    public void testHandleCommodityResponse() throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        File commodityGetFeatureResponse = new File("src/test/resources/commodityGetFeatureResponse.xml");
        BufferedReader reader = new BufferedReader( new FileReader(commodityGetFeatureResponse) );
        StringBuffer commodityGetFeatureResponseXML = new StringBuffer();

        String str;
        while ((str = reader.readLine()) != null) {
            commodityGetFeatureResponseXML.append(str);
        }
        reader.close();

        Collection<Commodity> commodities = 
            MineralOccurrencesResponseHandler.getCommodities(commodityGetFeatureResponseXML.toString());

        Assert.assertEquals("There are 2 commodities", 2, commodities.size());
        Assert.assertEquals("The first one's name is Gold", "Gold", ((Commodity)commodities.toArray()[0]).getCommodityName());
        Assert.assertEquals("The second one's MineralOccurrence source is urn:cgi:feature:GSV:MineralOccurrence:361170", "urn:cgi:feature:GSV:MineralOccurrence:361170", ((Commodity)commodities.toArray()[1]).getMineralOccurrenceURI());
    }
}
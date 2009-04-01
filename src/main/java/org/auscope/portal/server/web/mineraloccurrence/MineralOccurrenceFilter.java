package org.auscope.portal.server.web.mineraloccurrence;

/**
 * User: Michael Stegherr
 * Date: 26/03/2009
 * Time: 5:18:28 PM
 */
public class MineralOccurrenceFilter implements IFilter {
    private String minOreAmount;
    private String minCommodityAmount;
    private String cutOffGrade;

    public MineralOccurrenceFilter(String minOreAmount,
                                   String minCommodityAmount,
                                   String cutOffGrade) {
        this.minOreAmount       = minOreAmount;
        this.minCommodityAmount = minCommodityAmount;
        this.cutOffGrade        = cutOffGrade;
    }

    /**
     * Build the query string based on given properties
     * @return
     */
    public String getFilterString() {                  //TODO: this sucks! use geotools api to build queries...
        StringBuffer queryString = new StringBuffer();

        queryString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<wfs:GetFeature version=\"1.1.0\" xmlns:mo=\"urn:cgi:xmlns:GGIC:MineralOccurrence:1.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\"\n" +
                "        xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd\">\n" +
                "    <wfs:Query typeName=\"mo:MineralOccurrence\">\n" +
                "        <ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\">\n");

        if(checkMany())
            queryString.append("<ogc:And>");

        if(!this.minOreAmount.equals("")) // TODO use case states property: mo:OreMeasure:ore
            queryString.append("<ogc:PropertyIsGreaterThan>\n" +
                    "                   <ogc:PropertyName>mo:oreAmount/mo:Reserve/mo:ore/gsml:CGI_NumericValue/gsml:principalValue</ogc:PropertyName>\n" +
                    "                   <ogc:Literal>"+this.minOreAmount+"</ogc:Literal>\n" +
                    "           </ogc:PropertyIsGreaterThan>");

       if(!this.minCommodityAmount.equals("")) // TODO use case states property: mo:OreMeasure:measureDetails:CommodityMeasure:commodityAmount
            queryString.append("<ogc:PropertyIsGreaterThan>\n" +
                    "                   <ogc:PropertyName>mo:oreAmount/mo:Reserve/mo:measureDetails/mo:CommodityMeasure/mo:commodityAmount/gsml:CGI_NumericValue/gsml:principalValue</ogc:PropertyName>\n" +
                    "                   <ogc:Literal>"+this.minCommodityAmount+"</ogc:Literal>\n" +
                    "           </ogc:PropertyIsGreaterThan>");

        if(!this.cutOffGrade.equals("")) //TODO use case states property: mo:OreMeasure:measureDetails:CommodityMeasure:cutOffGrade
            queryString.append("<ogc:PropertyIsGreaterThan>\n" +
                    "                   <ogc:PropertyName>mo:producedMaterial/mo:Product/mo:grade/gsml:CGI_NumericValue/gsml:principalValue</ogc:PropertyName>\n" +
                    "                   <ogc:Literal>"+this.cutOffGrade+"</ogc:Literal>\n" +
                    "           </ogc:PropertyIsGreaterThan>");

        if(checkMany())
            queryString.append("</ogc:And>");

        queryString.append("</ogc:Filter>\n" +
                "    </wfs:Query>\n" +
                "</wfs:GetFeature>");

        return queryString.toString();

    }

    /**
     * Do more than one query parameter have a value
     * @return
     */
    private boolean checkMany() {
        int howManyHaveaValue = 0;

        if(!this.minOreAmount.equals(""))
            howManyHaveaValue++;
        if(!this.minCommodityAmount.equals(""))
            howManyHaveaValue++;
        if(!this.cutOffGrade.equals(""))
            howManyHaveaValue++;

        if(howManyHaveaValue >= 2)
            return true;

        return false;
    }

}
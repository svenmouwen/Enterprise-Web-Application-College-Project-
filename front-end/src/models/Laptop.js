import laptops from "@/assets/dummy-data.json";

export class Laptop {
    // Attributes
    articleNr;
    ean;
    brand;
    model;

    /**
     *
     * @param articleNr - Article Number ( specific)
     * @param ean - EAN number (Universal)
     * @param brand - Brand name
     * @param model - Model
     */
    constructor(articleNr, ean, brand, model) {
        this.articleNr = articleNr;
        this.ean = ean;
        this.brand = brand;
        this.model = model;
    }

    static generateLaptops(amount, lastIndex) {
        const laptopsToShow = amount || 50;
        const laptopsArray = [];
        const temp = laptops.slice(lastIndex || 0);

        for (let i = 0; i < laptopsToShow; i++) {
            if (i < temp.length) {
                const laptopData = temp[i];
                laptopsArray.push(new Laptop(laptopData.articleNr, laptopData.ean, laptopData.brand, laptopData.model));
            } else {
                break;
            }
        }
        return laptopsArray;
    }
}
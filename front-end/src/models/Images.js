export class Images {
    // Attributes
    id;
    laptop_ean;
    last_updated_on;
    status;
    status_updated_by;
    notes;

    /**
     *
     * @param laptop_ean - EAN of the laptop being worked on
     * @param status - status of the image creation
     * @param status_updated_by - who updated the status
     * @param notes - any notes or comments
     */
    constructor(laptop_ean, status, status_updated_by, notes) {
        this.laptop_ean = laptop_ean;
        this.status = status;
        this.status_updated_by = status_updated_by;
        this.notes = notes;
    }
}
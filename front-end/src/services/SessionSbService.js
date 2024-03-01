/**
 * Export class of sessionService with some properties
 *
 * @author MilanBouwmeester
 */
export class SessionSbService {
    BROWSER_STORAGE_ITEM_NAME;
    RESOURCE_URL;
    _currentToken;
    _currentAccount;

    /**
     * @param resourceURL - ResourceURL
     * @param browserStorageItemName - Item name of the browserStorage
     */
    constructor(resourceURL, browserStorageItemName) {
        this.BROWSER_STORAGE_ITEM_NAME = browserStorageItemName;
        this.RESOURCE_URL = resourceURL;
        this._currentToken = null;
        this._currentAccount = null;
        this.getTokenFromBrowserStorage();
    }

    /**
     * @param email - Email of the user
     * @param password - Password of the user
     * @returns {Promise<any|null>} - Return the user if the log in process is successful
     */
    async asyncSignIn(email, password) {
        try {
            // Get the information of the input fields
            const body = JSON.stringify({email: email, password: password});
            let response = await fetch(this.RESOURCE_URL + "/login", {
                method: 'POST', headers: {'Content-Type': 'application/json'}, body: body,
            });
            // Check if the response is ok
            if (response.ok) {
                // call the user
                let user = await response.json();
                console.log(user)

                // Save the user information in the browser storage
                this.saveTokenIntoBrowserStorage(response.headers.get('Authorization'), user);

                // Returns the user
                return user;
            } else {
                // Error during log in process
                console.error('Request failed with status:', response.status);
                return null;
            }
        } catch (error) {
            // Error during log in process
            console.error('Error during fetch:', error);
            return null;
        }
    }

    /**
     * @param token - Token of the logged-in user
     * @param account - Account of the logged-in user
     */
    saveTokenIntoBrowserStorage(token, account) {
        this._currentToken = token;
        this._currentAccount = account;
        if (token == null) {
            this._currentAccount = null;
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");
        } else {
            console.log("New token for " + account.name + ": " + token);
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, token);
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC", JSON.stringify(account));
        }
    }

    /**
     * @returns {string} - Returns the token of the logged-in user
     */
    getTokenFromBrowserStorage() {
        let token = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);

        if (token == null) {
            const localStorageToken = window.localStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);

            if (localStorageToken) {
                window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, localStorageToken);
                token = localStorageToken;
            }
        }

        return token;
    }

    /**
     * @returns {*} - Get the current token
     */
    get currentToken() {
        return this._currentToken;
    }

    /**
     * @returns {*} - Get the current user account
     */
    get currentAccount() {
        return this._currentAccount;
    }

    /**
     * Sign out function
     */
    signOut() {
        this.saveTokenIntoBrowserStorage(null, null);
    }

    /**
     * @returns {boolean} - Returns if the logged-in user is authenticated
     */
    isAuthenticated() {
        return this._currentAccount != null;
    }
}
// Import
import fetchIntercept from 'fetch-intercept';

/**
 * Export class of fetchInterceptor with some properties
 *
 * @author MilanBouwmeester
 */
export class FetchInterceptor {
    static theInstance;
    sessionService;
    unregister;

    /**
     * @param sessionService - SessionService
     */
    constructor(sessionService) {
        FetchInterceptor.theInstance = this;
        this.sessionService = sessionService;
        this.unregister = fetchIntercept.register(this);
        console.log("FetchInterceptor has been registered.");
    }
}
import { Injectable } from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HTTP_INTERCEPTORS,
    HttpResponse,
    HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { tap, catchError } from 'rxjs/operators';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
    constructor(private cookieService: CookieService) {}

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        if (req.method === 'OPTIONS') {
            return next.handle(req);
        }

        const token = this.cookieService.get('authToken');
        const accessToken = window.sessionStorage.getItem('accessToken');
        const authToken = accessToken || token;

        if (authToken) {
            req = req.clone({
                withCredentials: true,
                setHeaders: {
                    Authorization: `Bearer ${authToken}`,
                },
            });
        } else {
            req = req.clone({
                withCredentials: false,
            });
            console.log('No token found');
        }

        // Handle the request and inspect response/error
        return next.handle(req).pipe(
            tap((event: HttpEvent<any>) => {
                if (event instanceof HttpResponse) {
                    console.log(
                        `Request to ${req.url} succeeded with status: ${event.status}`
                    );
                }
            }),
            catchError((error: HttpErrorResponse) => {
                const status = error.status;
                console.error(
                    `Request to ${req.url} failed with status: ${status}`
                );

                switch (status) {
                    case 403:
                        console.error(
                            '403 Forbidden: Access denied. Check token or permissions.'
                        );
                        // Optional: Add custom logic, e.g., redirect to login
                        break;
                    case 404:
                        console.error(
                            '404 Not Found: The requested resource does not exist.'
                        );
                        break;
                    case 401:
                        console.error(
                            '401 Unauthorized: Authentication required.'
                        );
                        // Optional: Redirect to login or refresh token
                        break;
                    case 500:
                        console.error(
                            '500 Server Error: Something went wrong on the server.'
                        );
                        break;
                    default:
                        console.error(`Unhandled status code: ${status}`);
                }

                return throwError(() => error);
            })
        );
    }
}

export const httpInterceptorProviders = [
    {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpRequestInterceptor,
        multi: true,
    },
];

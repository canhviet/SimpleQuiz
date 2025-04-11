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
import { tap, catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../_services/auth.service';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
    constructor(private toastr: ToastrService, private authService: AuthService) { }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {

        const token = this.authService.getTokenData();

        if (token) {
            req = req.clone({
                withCredentials: true,
                setHeaders: {
                    Authorization: `Bearer ${token.accessToken}`,
                },
            });
        } else {
            req = req.clone({
                withCredentials: false,
            });
        }

        // Handle the request and inspect response/error
        return next.handle(req).pipe(
            tap((event: HttpEvent<any>) => {
                if (event instanceof HttpResponse) {
                    // this.toastr.success(
                    //     event.statusText || 'Success',
                    //     `Request Successful - ${event.status}`
                    // );
                }
            }),
            catchError((error: HttpErrorResponse) => {
                const status = error.status;

                switch (status) {
                    case 403:
                        this.toastr.error(
                            'Access denied. Check your permissions.',
                            'Forbidden (403)'
                        );
                        break;
                    case 404:
                        this.toastr.warning(
                            'The requested resource was not found.',
                            'Not Found (404)'
                        );
                        break;
                    case 401:
                        this.toastr.warning(
                            'You are not authorized. Please log in.',
                            'Unauthorized (401)'
                        );
                        break;
                    case 500:
                        this.toastr.error(
                            'An internal server error occurred. Please try again later.',
                            'Server Error (500)'
                        );
                        break;
                    default:
                        this.toastr.info(
                            `An unexpected error occurred (${status}).`,
                            'Error'
                        );
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

import { Injectable } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    Router,
    RouterStateSnapshot,
    UrlTree,
} from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard {
    constructor(private router: Router, private cookieService: CookieService) {}
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ):
        | Observable<boolean | UrlTree>
        | Promise<boolean | UrlTree>
        | boolean
        | UrlTree {
        const token = this.cookieService.get('token');
        const accessToken = window.sessionStorage.getItem('token');
        const authToken = accessToken || token;

        if (authToken) {
            return true;
        }

        this.router.navigate(['']);
        console.log('token not found');
        return false;
    }
}

import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CoordinateComponent } from './coordinate.component';
import { CoordinateDetailComponent } from './coordinate-detail.component';
import { CoordinatePopupComponent } from './coordinate-dialog.component';
import { CoordinateDeletePopupComponent } from './coordinate-delete-dialog.component';

export const coordinateRoute: Routes = [
    {
        path: 'coordinate',
        component: CoordinateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Coordinates'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'coordinate/:id',
        component: CoordinateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Coordinates'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const coordinatePopupRoute: Routes = [
    {
        path: 'coordinate-new',
        component: CoordinatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Coordinates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'coordinate/:id/edit',
        component: CoordinatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Coordinates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'coordinate/:id/delete',
        component: CoordinateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Coordinates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

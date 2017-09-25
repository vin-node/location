import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from '../../shared';
import {
    CoordinateService,
    CoordinatePopupService,
    CoordinateComponent,
    CoordinateDetailComponent,
    CoordinateDialogComponent,
    CoordinatePopupComponent,
    CoordinateDeletePopupComponent,
    CoordinateDeleteDialogComponent,
    coordinateRoute,
    coordinatePopupRoute,
} from './';

const ENTITY_STATES = [
    ...coordinateRoute,
    ...coordinatePopupRoute,
];

@NgModule({
    imports: [
        AppSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CoordinateComponent,
        CoordinateDetailComponent,
        CoordinateDialogComponent,
        CoordinateDeleteDialogComponent,
        CoordinatePopupComponent,
        CoordinateDeletePopupComponent,
    ],
    entryComponents: [
        CoordinateComponent,
        CoordinateDialogComponent,
        CoordinatePopupComponent,
        CoordinateDeleteDialogComponent,
        CoordinateDeletePopupComponent,
    ],
    providers: [
        CoordinateService,
        CoordinatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppCoordinateModule {}

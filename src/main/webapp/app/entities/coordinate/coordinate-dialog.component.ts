import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Coordinate } from './coordinate.model';
import { CoordinatePopupService } from './coordinate-popup.service';
import { CoordinateService } from './coordinate.service';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-coordinate-dialog',
    templateUrl: './coordinate-dialog.component.html'
})
export class CoordinateDialogComponent implements OnInit {

    coordinate: Coordinate;
    isSaving: boolean;

    locations: Location[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private coordinateService: CoordinateService,
        private locationService: LocationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.coordinate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.coordinateService.update(this.coordinate));
        } else {
            this.subscribeToSaveResponse(
                this.coordinateService.create(this.coordinate));
        }
    }

    private subscribeToSaveResponse(result: Observable<Coordinate>) {
        result.subscribe((res: Coordinate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Coordinate) {
        this.eventManager.broadcast({ name: 'coordinateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-coordinate-popup',
    template: ''
})
export class CoordinatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coordinatePopupService: CoordinatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.coordinatePopupService
                    .open(CoordinateDialogComponent as Component, params['id']);
            } else {
                this.coordinatePopupService
                    .open(CoordinateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

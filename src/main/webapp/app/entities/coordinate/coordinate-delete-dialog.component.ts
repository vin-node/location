import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Coordinate } from './coordinate.model';
import { CoordinatePopupService } from './coordinate-popup.service';
import { CoordinateService } from './coordinate.service';

@Component({
    selector: 'jhi-coordinate-delete-dialog',
    templateUrl: './coordinate-delete-dialog.component.html'
})
export class CoordinateDeleteDialogComponent {

    coordinate: Coordinate;

    constructor(
        private coordinateService: CoordinateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.coordinateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'coordinateListModification',
                content: 'Deleted an coordinate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-coordinate-delete-popup',
    template: ''
})
export class CoordinateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coordinatePopupService: CoordinatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.coordinatePopupService
                .open(CoordinateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

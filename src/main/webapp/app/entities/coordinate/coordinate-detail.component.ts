import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Coordinate } from './coordinate.model';
import { CoordinateService } from './coordinate.service';

@Component({
    selector: 'jhi-coordinate-detail',
    templateUrl: './coordinate-detail.component.html'
})
export class CoordinateDetailComponent implements OnInit, OnDestroy {

    coordinate: Coordinate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private coordinateService: CoordinateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCoordinates();
    }

    load(id) {
        this.coordinateService.find(id).subscribe((coordinate) => {
            this.coordinate = coordinate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCoordinates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'coordinateListModification',
            (response) => this.load(this.coordinate.id)
        );
    }
}

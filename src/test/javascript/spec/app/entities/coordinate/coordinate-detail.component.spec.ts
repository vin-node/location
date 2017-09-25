/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AppTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CoordinateDetailComponent } from '../../../../../../main/webapp/app/entities/coordinate/coordinate-detail.component';
import { CoordinateService } from '../../../../../../main/webapp/app/entities/coordinate/coordinate.service';
import { Coordinate } from '../../../../../../main/webapp/app/entities/coordinate/coordinate.model';

describe('Component Tests', () => {

    describe('Coordinate Management Detail Component', () => {
        let comp: CoordinateDetailComponent;
        let fixture: ComponentFixture<CoordinateDetailComponent>;
        let service: CoordinateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [CoordinateDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CoordinateService,
                    JhiEventManager
                ]
            }).overrideTemplate(CoordinateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CoordinateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoordinateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Coordinate(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.coordinate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});

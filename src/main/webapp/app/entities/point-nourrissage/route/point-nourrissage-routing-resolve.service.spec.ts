import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';
import { PointNourrissageService } from '../service/point-nourrissage.service';

import { PointNourrissageRoutingResolveService } from './point-nourrissage-routing-resolve.service';

describe('PointNourrissage routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PointNourrissageRoutingResolveService;
  let service: PointNourrissageService;
  let resultPointNourrissage: IPointNourrissage | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PointNourrissageRoutingResolveService);
    service = TestBed.inject(PointNourrissageService);
    resultPointNourrissage = undefined;
  });

  describe('resolve', () => {
    it('should return IPointNourrissage returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointNourrissage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointNourrissage).toEqual({ id: 123 });
    });

    it('should return new IPointNourrissage if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointNourrissage = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPointNourrissage).toEqual(new PointNourrissage());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PointNourrissage })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointNourrissage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointNourrissage).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

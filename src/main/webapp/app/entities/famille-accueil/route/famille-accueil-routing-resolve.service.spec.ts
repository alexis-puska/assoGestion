import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFamilleAccueil, FamilleAccueil } from '../famille-accueil.model';
import { FamilleAccueilService } from '../service/famille-accueil.service';

import { FamilleAccueilRoutingResolveService } from './famille-accueil-routing-resolve.service';

describe('FamilleAccueil routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FamilleAccueilRoutingResolveService;
  let service: FamilleAccueilService;
  let resultFamilleAccueil: IFamilleAccueil | undefined;

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
    routingResolveService = TestBed.inject(FamilleAccueilRoutingResolveService);
    service = TestBed.inject(FamilleAccueilService);
    resultFamilleAccueil = undefined;
  });

  describe('resolve', () => {
    it('should return IFamilleAccueil returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamilleAccueil = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFamilleAccueil).toEqual({ id: 123 });
    });

    it('should return new IFamilleAccueil if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamilleAccueil = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFamilleAccueil).toEqual(new FamilleAccueil());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FamilleAccueil })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamilleAccueil = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFamilleAccueil).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

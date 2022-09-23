import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVisiteVeterinaire, VisiteVeterinaire } from '../visite-veterinaire.model';
import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';

import { VisiteVeterinaireRoutingResolveService } from './visite-veterinaire-routing-resolve.service';

describe('VisiteVeterinaire routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VisiteVeterinaireRoutingResolveService;
  let service: VisiteVeterinaireService;
  let resultVisiteVeterinaire: IVisiteVeterinaire | undefined;

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
    routingResolveService = TestBed.inject(VisiteVeterinaireRoutingResolveService);
    service = TestBed.inject(VisiteVeterinaireService);
    resultVisiteVeterinaire = undefined;
  });

  describe('resolve', () => {
    it('should return IVisiteVeterinaire returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVisiteVeterinaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVisiteVeterinaire).toEqual({ id: 123 });
    });

    it('should return new IVisiteVeterinaire if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVisiteVeterinaire = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVisiteVeterinaire).toEqual(new VisiteVeterinaire());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VisiteVeterinaire })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVisiteVeterinaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVisiteVeterinaire).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

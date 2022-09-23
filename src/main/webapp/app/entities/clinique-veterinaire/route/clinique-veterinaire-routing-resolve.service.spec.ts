import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICliniqueVeterinaire, CliniqueVeterinaire } from '../clinique-veterinaire.model';
import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';

import { CliniqueVeterinaireRoutingResolveService } from './clinique-veterinaire-routing-resolve.service';

describe('CliniqueVeterinaire routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CliniqueVeterinaireRoutingResolveService;
  let service: CliniqueVeterinaireService;
  let resultCliniqueVeterinaire: ICliniqueVeterinaire | undefined;

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
    routingResolveService = TestBed.inject(CliniqueVeterinaireRoutingResolveService);
    service = TestBed.inject(CliniqueVeterinaireService);
    resultCliniqueVeterinaire = undefined;
  });

  describe('resolve', () => {
    it('should return ICliniqueVeterinaire returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCliniqueVeterinaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCliniqueVeterinaire).toEqual({ id: 123 });
    });

    it('should return new ICliniqueVeterinaire if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCliniqueVeterinaire = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCliniqueVeterinaire).toEqual(new CliniqueVeterinaire());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CliniqueVeterinaire })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCliniqueVeterinaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCliniqueVeterinaire).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

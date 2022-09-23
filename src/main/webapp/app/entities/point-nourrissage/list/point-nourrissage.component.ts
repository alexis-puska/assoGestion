import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointNourrissage } from '../point-nourrissage.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PointNourrissageService } from '../service/point-nourrissage.service';
import { PointNourrissageDeleteDialogComponent } from '../delete/point-nourrissage-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-point-nourrissage',
  templateUrl: './point-nourrissage.component.html',
})
export class PointNourrissageComponent implements OnInit {
  pointNourrissages: IPointNourrissage[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected pointNourrissageService: PointNourrissageService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.pointNourrissages = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.pointNourrissageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IPointNourrissage[]>) => {
          this.isLoading = false;
          this.paginatePointNourrissages(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.pointNourrissages = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPointNourrissage): number {
    return item.id!;
  }

  delete(pointNourrissage: IPointNourrissage): void {
    const modalRef = this.modalService.open(PointNourrissageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pointNourrissage = pointNourrissage;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePointNourrissages(data: IPointNourrissage[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.pointNourrissages.push(d);
      }
    }
  }
}

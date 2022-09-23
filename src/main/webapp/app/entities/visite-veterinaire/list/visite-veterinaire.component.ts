import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisiteVeterinaire } from '../visite-veterinaire.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';
import { VisiteVeterinaireDeleteDialogComponent } from '../delete/visite-veterinaire-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-visite-veterinaire',
  templateUrl: './visite-veterinaire.component.html',
})
export class VisiteVeterinaireComponent implements OnInit {
  visiteVeterinaires: IVisiteVeterinaire[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected visiteVeterinaireService: VisiteVeterinaireService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.visiteVeterinaires = [];
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

    this.visiteVeterinaireService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IVisiteVeterinaire[]>) => {
          this.isLoading = false;
          this.paginateVisiteVeterinaires(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.visiteVeterinaires = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVisiteVeterinaire): number {
    return item.id!;
  }

  delete(visiteVeterinaire: IVisiteVeterinaire): void {
    const modalRef = this.modalService.open(VisiteVeterinaireDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visiteVeterinaire = visiteVeterinaire;
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

  protected paginateVisiteVeterinaires(data: IVisiteVeterinaire[] | null, headers: HttpHeaders): void {
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
        this.visiteVeterinaires.push(d);
      }
    }
  }
}

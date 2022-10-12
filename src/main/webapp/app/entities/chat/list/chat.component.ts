import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChat } from '../chat.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { FileUtilsService } from 'app/shared/util/file-utils.service';
import { FileSaverService } from 'ngx-filesaver';
import { ChatDeleteDialogComponent } from '../delete/chat-delete-dialog.component';
import { ChatService } from '../service/chat.service';

@Component({
  selector: 'jhi-chat',
  templateUrl: './chat.component.html',
})
export class ChatComponent implements OnInit {
  chats: IChat[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  generateContrat: number[] = [];

  constructor(
    protected chatService: ChatService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected fileSaverService: FileSaverService
  ) {
    this.chats = [];
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

    this.chatService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IChat[]>) => {
          this.isLoading = false;
          this.paginateChats(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.chats = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IChat): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(chat: IChat): void {
    const modalRef = this.modalService.open(ChatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chat = chat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  downloadContrat(chat: IChat): void {
    if (chat.id) {
      this.generateContrat.push(chat.id);
      this.chatService.downloadContrat(chat.id).subscribe(
        res => {
          this.fileSaverService.save(res.body, FileUtilsService.getFileNameFromHeader(res.headers));
          this.generateContrat = this.generateContrat.filter((id: number) => id !== chat.id);
        },
        () => {
          this.generateContrat = this.generateContrat.filter((id: number) => id !== chat.id);
        }
      );
    }
  }

  isGenerateContrat(chatId: number | undefined): boolean {
    if (chatId) {
      return this.generateContrat.includes(chatId);
    }
    return false;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateChats(data: IChat[] | null, headers: HttpHeaders): void {
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
        this.chats.push(d);
      }
    }
  }
}

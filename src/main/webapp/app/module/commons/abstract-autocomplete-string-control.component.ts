import { Component, Input, OnInit } from '@angular/core';
import { ControlValueAccessor } from '@angular/forms';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { debounceTime, filter, map, switchMap, tap } from 'rxjs/operators';

/**
 * Abstract component for searching in a ng select with AUTOCOMPLETE. Need to specify the type of object.
 * See cl-autocomplete-control.component.ts for example implementation.
 *
 * OC Abstract-search-control.component.ts
 */
@Component({
  template: '',
})
export abstract class AbstractAutocompleteControlStringComponent<Content extends string> implements OnInit, ControlValueAccessor {
  @Input() multiple = false;

  // does not work: if using bindValues, label automatically set itself to this value ???
  // @Input()
  // bindValue: string;

  /**
   * Override this subject if you want to manually trigger a refresh of the items.
   * Used in case of external modification to the list (to re-fetch display newly added items).
   */
  @Input() refresh$ = new BehaviorSubject<any>(null);

  numberOfChars = 2;
  isListItemLoading = false;
  disabled = false;

  items$: Observable<Content[]> | null = null;
  itemQuery$ = new BehaviorSubject<string>('');

  private currentItem$ = new BehaviorSubject<Content | Content[] | null>(null);
  private itemSaved: Content | Content[] | null = null;

  get item(): Content | Content[] | null {
    return this.itemSaved;
  }

  set item(v: Content | Content[] | null) {
    this.itemSaved = v;
    this.currentItem$.next(v);
    this.onChange(this.item);
  }

  protected constructor() {}

  ngOnInit(): void {
    const q$ = combineLatest([this.itemQuery$, this.refresh$]).pipe(
      map(([itemQ]) => itemQ),
      filter((query: string) => query?.trim().length >= this.numberOfChars),
      debounceTime(330),
      tap(() => (this.isListItemLoading = true)),
      switchMap(query => this.queryWith(query)),
      tap({
        next: () => (this.isListItemLoading = false),
        error: () => (this.isListItemLoading = false),
      })
    );

    // take result list from query and add current selected item to the top of the items list
    this.items$ = combineLatest([this.currentItem$, q$]).pipe(
      map(([resCurrent, resQuery]) => {
        if (!Array.isArray(resCurrent)) {
          resCurrent = !resCurrent ? [] : [resCurrent];
        }
        return [resCurrent, resQuery];
      }),
      map(([resCurrent, resQuery]) => {
        // filters out the currently selected item from result list, in order to prevent the item
        // appearing both at the top of the list and in the list itself
        resQuery = resQuery?.filter(q => !resCurrent.find(c => c === q));
        return resCurrent.concat(resQuery);
      })
    );
  }

  /**
   * The method that will handle user query for typeAhead. Should return an array of items that
   * is the result of the query the user typed. (like a simplified search method with only 1 param)
   */
  abstract queryWith(query: string): Observable<Content[]>;

  writeValue(item: Content | Content[] | null): void {
    // appelée pour la valeur par default
    this.item = item;
  }

  registerOnChange(fn: (item: Content | Content[] | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  clearValue(): void {
    this.item = this.multiple ? [] : null;
  }
  /* eslint-disable */
  private onChange(item: Content | Content[] | null): void {}
  /* eslint-enable */
  private onTouched(): void {}
}

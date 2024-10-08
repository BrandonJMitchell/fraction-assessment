import {Pageable} from "./pageable";
import {Sort} from "./sort";

export interface Page<T> {
  content: Array<T>;
  pageable: Pageable;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  empty: boolean;

}

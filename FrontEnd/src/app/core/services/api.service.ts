import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string, params?: Record<string, any>, headers?: HttpHeaders): Observable<T> {
    if (!endpoint || endpoint.trim() === '') {
      throw new Error('Endpoint is required for GET request');
    }

    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.append(key, params[key].toString());
        }
      });
    }

    return this.http.get<T>(`${this.apiUrl}/${endpoint}`, { params: httpParams, headers });
  }

  post<T>(endpoint: string, body: any, headers?: HttpHeaders): Observable<T> {
    if (!endpoint || endpoint.trim() === '') {
      throw new Error('Endpoint is required for POST request');
    }
    return this.http.post<T>(`${this.apiUrl}/${endpoint}`, body, { headers });
  }

  put<T>(endpoint: string, body: any, headers?: HttpHeaders): Observable<T> {
    if (!endpoint || endpoint.trim() === '') {
      throw new Error('Endpoint is required for PUT request');
    }
    return this.http.put<T>(`${this.apiUrl}/${endpoint}`, body, { headers });
  }

  delete<T>(endpoint: string, headers?: HttpHeaders): Observable<T> {
    if (!endpoint || endpoint.trim() === '') {
      throw new Error('Endpoint is required for DELETE request');
    }
    return this.http.delete<T>(`${this.apiUrl}/${endpoint}`, { headers });
  }
}

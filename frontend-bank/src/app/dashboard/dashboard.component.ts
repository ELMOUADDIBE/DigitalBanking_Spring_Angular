import { Component } from '@angular/core';
import { ChartData, ChartType, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  // Line Chart Data
  public lineChartData: ChartData<'line'> = {
    datasets: [
      { data: [65, 59, 80, 81, 56, 55, 40], label: 'Total Balance' }
    ],
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July']
  };
  public lineChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true }
    }
  };
  public lineChartType: ChartType = 'line';

  // Bar Chart Data
  public barChartData: ChartData<'bar'> = {
    datasets: [
      { data: [50, 60, 70, 180, 190], label: 'Deposits' },
      { data: [30, 45, 55, 70, 80], label: 'Withdrawals' }
    ],
    labels: ['Q1', 'Q2', 'Q3', 'Q4', 'Q5']
  };
  public barChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true }
    }
  };
  public barChartType: ChartType = 'bar';

  // Pie Chart Data
  public pieChartData: ChartData<'pie'> = {
    datasets: [{ data: [300, 500, 100] }],
    labels: ['Retail', 'Corporate', 'SME']
  };
  public pieChartType: ChartType = 'pie';
}

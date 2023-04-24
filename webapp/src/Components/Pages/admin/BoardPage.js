// eslint-disable-next-line import/no-extraneous-dependencies
import { Chart } from 'chart.js/auto';
import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
// eslint-disable-next-line no-unused-vars
import API from '../../../utils/api'

const BoardPage = () => {
    const authenticatedUser = getAuthenticatedUser();

    if (!authenticatedUser || authenticatedUser.role === null) {
      Navigate('/');
      return;
    }

  clearPage();
  renderBoardPage();
  renderBoardData();
};

function renderBoardPage(){
    const main = document.querySelector('main');
    const board = document.createElement('div');

    
    board.innerHTML=`
    <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">Tableau de bord</h1>
                    </div>

                    <!-- Content Row -->
                    <div class="row">

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-primary shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                                Earnings (Monthly)</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800">TO DO</div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-calendar fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-success shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                                Earnings (Annual)</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            
                                            <div id=nbOffers>
                                            
                                            </div>
                                            
                                            </div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <!-- Content Row -->

                    <div class="row">

                        <!-- Area Chart -->
                        <div class="col-xl-8 col-lg-7">
                            <div class="card shadow mb-4">
                                <!-- Card Header - Dropdown -->
                                <div
                                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h6 class="m-0 font-weight-bold text-primary">Suivi des objets</h6>
                                    <div class="dropdown no-arrow">
                                    </div>
                                </div>

                                <!-- Card Body -->
                                <div class="card-body">
                                    <div id="chart-area">
                                        <label for="bday-month">Période:</label>
                                        <input id="bday-month" type="month" name="bday-month" value="2023-03">
                                        <br>

                                        <div class="dashboard">
                                            <div class="statistics">
                                                <h2>Statistiques</h2>
                                                <ul>
                                                    <li>Nombre d'objets : <div id="totalObjects"></div></li>
                                                    <li>Nombre d'objets proposés : <div id="offeredObjects"></div></li>
                                                    <li>Nombre d'objets vendus : <div id="soldObjects"></div></li>
                                                    <li>Nombre d'objets acceptés : <div id="acceptedObjects"></div></li>
                                                    <li>Nombre d'objets refusés : <div id="rejectedObjects"></div></li>
                                                </ul>
                                            </div>

                                            <div class="chart">
                                            <h2>Graphique</h2>
                                                <canvas id="myChart"></canvas>
                                            </div>
                                      </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.container-fluid -->
    `;

    API.get(`objects/offers?query=${encodeURIComponent('')}`).then((offers) => {
        const count = offers.length;
        document.getElementById('offeredObjects').innerHTML =`
        ${count}
        ` ;
    });

    API.get(`objects/`).then((objects) => {
        const count = objects.length;
        document.getElementById('totalObjects').innerHTML =`
        ${count}
        ` ;

        
        const vendu = objects.filter(object => object.state === 'vendu');
        // eslint-disable-next-line no-console
        console.log(vendu)
        document.getElementById('soldObjects').innerHTML =`
        ${vendu}
        ` ;
        
    });


    main.appendChild(board);
}

function renderBoardData(){

        const totalObjects = 100;
        const soldObjects = 30;
        const acceptedObjects = 60;
        const rejectedObjects = 10;
        
    
        const chartElement = document.getElementById("myChart");
        
        
        // Création du graphique
        const chartData = {
          labels: ["Proposés", "Vendus", "Acceptés", "Refusés"],
          datasets: [
            {
              label: "Nombre d'objets",
              data: [totalObjects, soldObjects, acceptedObjects, rejectedObjects],
              backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(75, 192, 192, 0.2)",
              ],
              borderColor: [
                "rgba(255, 99, 132, 1)",
                "rgba(54, 162, 235, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(75, 192, 192, 1)",
              ],
              borderWidth: 1,
            },
          ],
        };
        
        const chartOptions = {
          
        };

        const myChart = new Chart(chartElement, {
          type: 'pie',
          data: chartData,
          options: chartOptions,
        });
        
        document.getElementById("myChart").appendChild(myChart.canvas);
    
    }

export default BoardPage;
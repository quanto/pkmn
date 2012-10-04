package map

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 19:38
 * To change this template use File | Settings | File Templates.
 */
enum View {

    ShowMap, ShowMarket, ShowComputer, ChosePokemon, Battle

    /*
    include_once($mapsPath."/createMaps.php");
	static function checkView()
	{
		$viewNumber = View::getView();

		$frame = "<iframe src='!!url!!' frameborder='0' width='500' height='300'></iframe>";

		if($viewNumber == 0) //Show map
		{
			$view = createMaps::createTheMap();
		}
		elseif($viewNumber == 1) //Show market
		{
			$view = str_replace("!!url!!", "battleEngine/markt.php", $frame);
		}
		elseif($viewNumber == 2) //Show computer
		{
			$view = str_replace("!!url!!", "computer.php", $frame);
		}
		elseif($viewNumber == 3) //start page
		{
			$view = "";
        }
        elseif($viewNumber == 4)//Battle
        {
            $view = str_replace("!!url!!", "battleEngine/index.php", $frame);
        }

        return $view;
    }

    static function getView()
    {
        $sql = mysql_query("SELECT view FROM playerdata where id = ".$_SESSION['id']) or die(mysql_error());

        $result = mysql_fetch_row($sql);

        return $result[0];
    }

    static function setView($view)
    {
        $sql = mysql_query("UPDATE playerdata SET view = ".$view."  WHERE id = ".$_SESSION['id']) or die(mysql_error());
    }
    */
}

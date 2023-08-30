package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import una.instrumentos.logic.Service;

import java.util.List;

public class Controller{
	public Controller(View view, Model model) {
		model.init(Service.instance().search(new TipoInstrumento()));
		this.view = view;
		this.model = model;
		view.setController(this);
		view.setModel(model);
	}
	public void search(TipoInstrumento filter) throws  Exception{
		List<TipoInstrumento> rows = Service.instance().search(filter);
		if (rows.isEmpty()){
			throw new Exception("NINGUN REGISTRO COINCIDE");
		}
		model.setList(rows);
		model.setCurrent(new TipoInstrumento());
		model.commit();
	}
	public void edit(int row){
		TipoInstrumento e = model.getList().get(row);
		try {
			model.setCurrent(Service.instance().read(e));
			model.commit();
		} catch (Exception ex) {}
	}
	View view;
	Model model;

	public void save(TipoInstrumento tipoInstrumento) {
		try {
			Service.instance().create(tipoInstrumento);
			model.setList(Service.instance().search(new TipoInstrumento()));
			model.setCurrent(new TipoInstrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

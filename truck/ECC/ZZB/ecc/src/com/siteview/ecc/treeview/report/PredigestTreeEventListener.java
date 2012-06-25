package com.siteview.ecc.treeview.report;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.siteview.ecc.treeview.controls.BaseTreeitem;

/**
 * 简化树事件类,主要用于点击树节点时,选取相关节点. 1.树展开时判断当前节点选项状态,决定所有子节点(包括叶节点)状态; 方法一:
 * 所有子点节点孙节点与自已状态相同的方法
 * 
 * 2.点节点A,若所有同级节点有一个选中,父节点选中,状态一直影响到顶级节点, 方法二: 所有父节点,祖节点选中的方法
 * 
 * 3.没有一个选中,父节点不选中;上去N级节点状态都与各自子节点状态相关 方法三: 父节点不选中,各级祖节点进行遍历,有一个被选中,则该祖节点选中
 * 
 * 4.所有子节点与A状态相同; 同方法一
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-3-24
 */
public class PredigestTreeEventListener implements EventListener {
	private final static Logger logger = Logger.getLogger(PredigestTreeEventListener.class);

	/**
	 * 方法一: 所有子点节点孙节点与自已状态相同的方法
	 * 
	 * @param t
	 */
	public void setChildrenSameSelf(BaseTreeitem t) {
		if (t != null) {
			/*
			 * logger.info(t.getLabel() + "**>>>" + "::::::" +
			 * t.isSelected()); Set set = t.getTree().getSelectedItems();
			 * Iterator it = set.iterator(); while (it.hasNext()) { Treeitem y =
			 * (Treeitem) it.next(); logger.info("---------" +
			 * y.getLabel()); }
			 */
			// 3.6.0版本的ZKOSS t.isSelected()永远返回true,以下方法不能正确执行
			// if (t.isSelected()) {// 当前节点被选中,所有子节点,孙节点也应选中
			if (t.isChecked()) {
				// if (t.getTree().getSelectedItem() == t) {
				// logger.info(t.getLabel() + " 被选中了");
				selectedAllChildren(t, true);
			} else {// 当前节点没有被选中,所有子节点,孙节点也应不选中
				unselectedAllChildren(t);
			}
			t = null;
		}

	}

	/**
	 * 方法二: 所有父节点,祖节点选中的方法
	 * 
	 * @param r
	 */
	public void selectAllParent(Object r) {
		if (r != null) {
			Object p = null;
			if (r instanceof Treeitem) {
				((Treeitem) r).setSelected(true);
				setT(r);
				p = ((Treeitem) r).getParent();
				selectAllParent(p);
			} else if (r instanceof Treechildren) {
				p = ((Treechildren) r).getParent();
				selectAllParent(p);
			} else if (r instanceof Tree) {

			} else {
				log(r + " in selectAllParent方法中");
			}

		}
	}

	/**
	 * 方法三: 父节点不选中,各级祖节点进行遍历,有一个被选中,则该祖节点选中
	 * 
	 * @param p
	 */
	public void selectParentAndLookForefathers(Treeitem p) {
		unselectedParent(p.getParent());
		lookForefathers(p.getParent());
	}

	/**
	 * 
	 * 
	 * @param t
	 */
	/**
	 * 选中所有子孙节点
	 * 
	 * @param t
	 * @param need
	 *            need为true时选中,为false时不选中
	 */
	private void selectedAllChildren(Object t, boolean need) {
		List tcl = null;
		if (t instanceof Treechildren) {
			tcl = ((Treechildren) t).getChildren();
		} else if (t instanceof Treeitem) {
			tcl = ((Treeitem) t).getChildren();
		}
		if (tcl != null) {
			List lit = null;
			for (Object o : tcl) {
				if (o instanceof Treechildren) {
					lit = ((Treechildren) o).getChildren();
					for (Object x : lit) {
						if (x instanceof Treechildren) {
							selectedAllChildren(x, need);
						} else if (x instanceof Treeitem) {
							((Treeitem) x).setSelected(need);
							stf(x, need);
							selectedAllChildren((Treeitem) x, need);
						} else {
							log(x + " in  selectedAllChildren 2");
						}
					}

				} else if (o instanceof Treeitem) {
					((Treeitem) o).setSelected(need);
					stf(o, need);
					selectedAllChildren((Treeitem) o, need);
				} else if (o instanceof Treerow) {
					((Treeitem) ((Treerow) o).getParent()).setSelected(need);
					stf(((Treerow) o).getParent(), need);
				} else {
					log(o + " in  selectedAllChildren 1");
				}
			}
			tcl = null;
			lit = null;
		}
	}

	/**
	 * 遍历祖节点,有一个被选中,则该祖节点选中
	 * 
	 * @param t
	 */
	private void lookForefathers(Object t) {
		Object j = null;
		if (t != null) {
			if (t instanceof Treeitem) {
				if (jugdecurrent((Treeitem) t)) {// 当前节点被决定于选中了,不再继续向上找了
					j = null;
				} else {
					j = ((Treeitem) t).getParent();
				}
			} else if (t instanceof Treechildren) {// 继续向上找
				lookForefathers(((Treechildren) t).getParent());
			} else if (t instanceof Tree) {// 到顶了
			} else {
				log(t + " in lookForefathers 3");
			}
		}

		if (j != null) {
			if (j instanceof Treeitem) {
				List<Object> litc = ((Treeitem) j).getChildren();
				// if (litc != null && litc.size() > 0) {
				for (Object o : litc) {
					if (o instanceof Treeitem) {
						// if (((Treeitem) o).isSelected()) {// 有一个节点选中
						if (isc(o)) {
							((Treeitem) j).setSelected(true);// 该祖节点选中
							setT(j);
							break;// 退出循环
						}
					} else {
						log(o + " in lookForefathers 2");
					}
				}
				// }
				lookForefathers(j);
			} else if (j instanceof Treechildren) {
				lookForefathers(((Treechildren) j).getParent());
			} else {
				log(j + " in lookForefathers 1");
			}
		}
	}

	/**
	 * 判断当前节点是否应被选中,决定于下一级子节点,若有一个子节点被选中,则选中当前节点,否则反之
	 * 
	 * @param m
	 */
	private boolean jugdecurrent(Treeitem m) {
		if (m == null)
			return false;
		m.setSelected(false);// 默认为不选中
		setF(m);
		List<Object> litc = m.getChildren();
		for (Object o : litc) {// 遍历子节点
			if (o instanceof Treeitem) {
				// if (((Treeitem) o).isSelected()) {// 有一个节点选中
				if (isc(o)) {
					m.setSelected(true);// 该祖节点选中
					setT(m);
					return true;
				}
			} else if (o instanceof Treechildren) {
				List tb = ((Treechildren) o).getChildren();
				for (Object e : tb) {
					if (e instanceof Treeitem) {
						// if (((Treeitem) e).isSelected()) {
						if (isc(e)) {
							m.setSelected(true);// 该祖节点选中
							setT(m);
							return true;
						}
					} else {
						log(e + " in jugdecurrent");
					}
				}

			} else if (o instanceof Treerow) {
				// if (((Treeitem) ((Treerow) o).getParent()).isSelected()) {
				if (isc(((Treerow) o).getParent())) {
					m.setSelected(true);// 该祖节点选中
					setT(m);
					return true;
				}
			} else {
				log(o + " in lookForefathers 4");
			}
		}
		litc = null;
		return false;
	}

	/**
	 * 不选中所有子孙节点
	 * 
	 * @param t
	 */
	private void unselectedAllChildren(Treeitem t) {
		selectedAllChildren(t, false);
	}

	/**
	 * 不选中父节点
	 * 
	 * @param t
	 */
	private void unselectedParent(Object t) {
		if (t != null) {
			if (t instanceof Treeitem) {
				((Treeitem) t).setSelected(false);
				setF(t);
			} else if (t instanceof Treechildren) {
				unselectedParent(((Treechildren) t).getParent());
			} else if (t instanceof Tree) {

			} else {
				log(t + " in unselectedParent");
			}

		}
	}

	public void onEvent(Event event) {
		Treerow tr = (Treerow) event.getTarget();
		BaseTreeitem tri = (BaseTreeitem) tr.getParent();
		// tri.getTree().
		// logger.info("当前节点:" + tri.getLabel() + "--" + tri.is);
		/*
		 * try { Messagebox.show(tri.getLabel()+tri.isSelected()); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		List li = null;
		if (tri != null) {
			if (tri.isChecked()) {
				tri.setChecked(false);
			} else {
				tri.setChecked(true);
			}
			if (tri.getParent() != null
					&& tri.getParent() instanceof Treechildren) {
				li = tri.getParent().getChildren();// 所有同级节点
			} else {
				li = tri.getChildren();// 所有同级节点
			}
		}

		boolean isselect = false;// 判断有没有同级节点被选中,选中为true
		if (li != null) {
			Object o = null;

			for (int a = 0; a < li.size(); a++) {// 遍历同级节点(兄弟节点)
				o = li.get(a);
				if (o instanceof Treerow) {// o为 叶节点
					Treeitem t = (Treeitem) (((Treerow) o).getParent());
					// if (t.isSelected()) {// 同级节点被选中
					if (isc(t)) {
						selectAllParent(t.getParent());// 所有父,祖节点被选中;调方法二
						isselect = true;
						break;// 不再遍历了
					}
				} else if (o instanceof Treeitem) {// o为非叶节点
					// if (((Treeitem) o).isSelected()) {// 同级节点被选中
					if (isc(o)) {
						selectAllParent(((Treeitem) o).getParent());//所有父,祖节点被选中
						// ;调方法二
						isselect = true;
						break;// 不再遍历了
					}
				} else if (o instanceof Treechildren) {
					// 不是同一级节点了,不处理
				} else {
					log(o + " in  onEvent 方法中");
				}
			}
			o = null;
		}
		if (!isselect) {// 没有同级节点选中,调方法三
			selectParentAndLookForefathers(tri);
		}
		setChildrenSameSelf(tri);// 所有子孙节点与自已状态相同
	}

	private boolean isc(Object o) {
		if (o != null) {
			return (((BaseTreeitem) o).isChecked());
		}
		return false;
	}

	private void setT(Object s) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(true);
		}
	}

	private void setF(Object s) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(false);
		}
	}

	private void stf(Object s, boolean b) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(b);
		}
	}

	private void log(Object o) {
		logger.info("发现重大BUG,通知di.tang处理:" + o);
	}
}
